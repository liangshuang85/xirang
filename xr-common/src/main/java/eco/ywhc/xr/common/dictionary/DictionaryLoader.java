package eco.ywhc.xr.common.dictionary;

import eco.ywhc.xr.common.annotation.DictionaryEntryConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 数据字典
 */
@Slf4j
public class DictionaryLoader {

    private static final String constantPackage = "eco.ywhc.xr.common.constant";

    private static final List<DictionaryEntry> entries = new ArrayList<>();

    private MessageSource messageSource;

    public static DictionaryLoader createWith(MessageSource messageSource) {
        DictionaryLoader loader = new DictionaryLoader();
        loader.messageSource = messageSource;
        loader.initialize();
        return loader;
    }

    private void initialize() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(DictionaryEntryConstant.class));
        for (BeanDefinition beanDef : provider.findCandidateComponents(constantPackage)) {
            Class<?> clazz;
            try {
                clazz = Class.forName(beanDef.getBeanClassName());
            } catch (ClassNotFoundException e) {
                log.warn("{}", e.getMessage());
                continue;
            }
            Object[] enumConstants = clazz.getEnumConstants();
            if (enumConstants == null) {
                continue;
            }
            var entries = Arrays.stream(enumConstants)
                    .filter(i -> {
                        try {
                            Field field = i.getClass().getField(i.toString());
                            var annotation = field.getAnnotation(DictionaryEntryConstant.class);
                            return annotation == null || annotation.value();
                        } catch (NoSuchFieldException e) {
                            log.warn("{}", e.getMessage());
                            return false;
                        }
                    })
                    .sorted((a, b) -> {
                        try {
                            Field fieldA = a.getClass().getField(a.toString());
                            Field fieldB = a.getClass().getField(b.toString());
                            Optional<Order> orderA = Optional.ofNullable(fieldA.getAnnotation(Order.class));
                            Optional<Order> orderB = Optional.ofNullable(fieldB.getAnnotation(Order.class));
                            return orderA.map(Order::value).orElse(Ordered.HIGHEST_PRECEDENCE) - orderB.map(Order::value).orElse(Ordered.HIGHEST_PRECEDENCE);
                        } catch (NoSuchFieldException e) {
                            return 0;
                        }
                    })
                    .map(i -> DictionaryEntry.of(i.getClass().getName() + "." + i, i.toString(), null))
                    .toList();
            DictionaryLoader.entries.add(DictionaryEntry.of(clazz.getName(), clazz.getSimpleName(), entries));
        }
        entries.sort(Comparator.comparing(DictionaryEntry::getValue));
    }

    public List<DictionaryEntry> getEntries() {
        return getEntries(LocaleContextHolder.getLocale());
    }

    public List<DictionaryEntry> getEntries(Locale locale) {
        return resolveEntries(locale);
    }

    public List<DictionaryEntry> getEntries(String entryName) {
        return getEntries(entryName, LocaleContextHolder.getLocale());
    }

    public List<DictionaryEntry> getEntries(String entryName, Locale locale) {
        if (StringUtils.isBlank(entryName)) {
            return Collections.emptyList();
        }
        var subEntries = entries.stream()
                .filter(i -> Objects.equals(i.getValue(), entryName))
                .findFirst()
                .map(DictionaryEntry::getEntries)
                .orElse(Collections.emptyList());
        return resolveEntries(subEntries, locale);
    }

    private List<DictionaryEntry> resolveEntries(Locale locale) {
        return resolveEntries(entries, locale);
    }

    private List<DictionaryEntry> resolveEntries(List<DictionaryEntry> entries, Locale locale) {
        var finalLocale = Optional.ofNullable(locale).orElse(LocaleContextHolder.getLocale());
        return entries.stream()
                .map(item -> {
                    String localizedLabel = messageSource.getMessage(item.getLabel(), null, item.getLabel(), finalLocale);
                    var subEntries = item.getEntries();
                    if (subEntries != null) {
                        subEntries = resolveEntries(subEntries, finalLocale);
                    }
                    return DictionaryEntry.of(localizedLabel, item.getValue(), subEntries);
                })
                .toList();
    }

}
