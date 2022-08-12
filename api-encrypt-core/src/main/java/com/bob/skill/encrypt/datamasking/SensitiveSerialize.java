package com.bob.skill.encrypt.datamasking;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * 序列化信息脱敏处理
 *
 * @author Bob
 * @date 2022/8/9 10:43
 */
public class SensitiveSerialize extends JsonSerializer<String> implements ContextualSerializer {

    /**
     * 脱敏类型
     */
    private SensitiveTypeEnum sensitiveTypeEnum;

    /**
     * 前几位不脱敏
     */
    private Integer prefixNoMaskLength;

    /**
     * 最后几位不脱敏
     */
    private Integer suffixNoMaskLength;

    /**
     * 用什么打码
     */
    private String symbol;

    public SensitiveSerialize() {
    }

    public SensitiveSerialize(SensitiveTypeEnum sensitiveTypeEnum, Integer prefixNoMaskLen, Integer suffixNoMaskLen, String symbol) {
        this.sensitiveTypeEnum = sensitiveTypeEnum;
        this.prefixNoMaskLength = prefixNoMaskLen;
        this.suffixNoMaskLength = suffixNoMaskLen;
        this.symbol = symbol;
    }

    @Override
    public void serialize(final String origin, final JsonGenerator jsonGenerator,
                          final SerializerProvider serializerProvider) throws IOException {
        switch (sensitiveTypeEnum) {
            case CUSTOMER:
                jsonGenerator.writeString(DesensitizedUtils.desValue(origin, prefixNoMaskLength, suffixNoMaskLength, symbol));
                break;
            case NAME:
                jsonGenerator.writeString(DesensitizedUtils.chineseName(origin));
                break;
            case ID_NUM:
                jsonGenerator.writeString(DesensitizedUtils.idCardNum(origin));
                break;
            case PHONE_NUM:
                jsonGenerator.writeString(DesensitizedUtils.mobilePhone(origin));
                break;
            default:
                throw new IllegalArgumentException("unknown sensitive type enum " + sensitiveTypeEnum);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider,
                                              final BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty == null) {
            return serializerProvider.findNullValueSerializer(null);
        }

        if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
            DataMasking dataMasking = beanProperty.getAnnotation(DataMasking.class);
            if (dataMasking == null) {
                dataMasking = beanProperty.getContextAnnotation(DataMasking.class);
            }
            if (dataMasking != null) {
                return new SensitiveSerialize(dataMasking.type(), dataMasking.prefixNoMaskLength(), dataMasking.suffixNoMaskLength(), dataMasking.symbol());
            }
        }
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }
}
