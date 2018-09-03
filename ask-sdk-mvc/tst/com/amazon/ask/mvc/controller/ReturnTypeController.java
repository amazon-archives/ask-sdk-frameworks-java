package com.amazon.ask.mvc.controller;

import com.amazon.ask.mvc.Utils;
import com.amazon.ask.mvc.annotation.mapping.IntentMapping;

public class ReturnTypeController {
    @IntentMapping(name = "voidReturnType")
    public void voidReturnType() {}

    @IntentMapping(name = "badResultType")
    public Object badResultType() {
        return new Object();
    }

    @IntentMapping(name = "nullResultValue")
    public Object nullResultValue() {
        return null;
    }

    @IntentMapping(name = "speechletResponseResultValue")
    public Object speechletResponseResultValue() {
        return Utils.EMPTY_RESPONSE;
    }

}
