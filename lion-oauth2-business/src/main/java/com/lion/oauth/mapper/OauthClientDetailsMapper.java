package com.lion.oauth.mapper;

import com.lion.oauth.dto.CuOauthClientDetailsDto;
import com.lion.oauth.entity.OauthClientDetails;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Mr.Liu
 * @classname CuOauthClientDetailsMapper
 * @description
 * @date 2022/04/16 上午11:46
 */
@Mapper
public interface OauthClientDetailsMapper {

    OauthClientDetailsMapper INSTANCE  = Mappers.getMapper(OauthClientDetailsMapper.class);

    OauthClientDetails CuOauthClientDetailsDtoToOauthClientDetails(CuOauthClientDetailsDto cuOauthClientDetailsDto);
}
