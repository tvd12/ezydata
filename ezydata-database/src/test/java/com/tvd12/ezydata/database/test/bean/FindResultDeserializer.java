package com.tvd12.ezydata.database.test.bean;

import com.tvd12.ezydata.database.annotation.EzyResultDeserialize;
import com.tvd12.ezydata.database.converter.EzyResultDeserializer;

@SuppressWarnings("rawtypes")
@EzyResultDeserialize(FindResult.class)
public class FindResultDeserializer implements EzyResultDeserializer {
}
