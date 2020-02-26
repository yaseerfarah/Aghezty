package com.example.aghezty.POJO;

import com.google.gson.annotations.SerializedName;

public class RegisterParentResponse {

    @SerializedName("success")
    private  RegisterResponse registerResponse;


    public RegisterResponse getRegisterResponse() {
        return registerResponse;
    }

    public void setRegisterResponse(RegisterResponse registerResponse) {
        this.registerResponse = registerResponse;
    }
}
