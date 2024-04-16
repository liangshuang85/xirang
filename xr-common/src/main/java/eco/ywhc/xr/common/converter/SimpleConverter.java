package eco.ywhc.xr.common.converter;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface SimpleConverter<E, Req, Res> {

    E fromRequest(Req source);

    void update(Req source, @MappingTarget E target);

    Res toResponse(E source);

    List<Res> toResponse(List<E> source);

}
