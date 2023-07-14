package com.yukikyu.kook.boot.app.service.mapper;

import com.yukikyu.kook.boot.app.domain.HelpStatistics;
import com.yukikyu.kook.boot.app.service.dto.HelpStatisticsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HelpStatistics} and its DTO {@link HelpStatisticsDTO}.
 */
@Mapper(componentModel = "spring")
public interface HelpStatisticsMapper extends EntityMapper<HelpStatisticsDTO, HelpStatistics> {}
