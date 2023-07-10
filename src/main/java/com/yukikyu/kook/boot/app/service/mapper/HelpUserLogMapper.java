package com.yukikyu.kook.boot.app.service.mapper;

import com.yukikyu.kook.boot.app.domain.HelpUserLog;
import com.yukikyu.kook.boot.app.service.dto.HelpUserLogDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HelpUserLog} and its DTO {@link HelpUserLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface HelpUserLogMapper extends EntityMapper<HelpUserLogDTO, HelpUserLog> {}
