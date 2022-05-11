package com.oldaim.fkbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TargetInfoService targetInfoService;

    public void testMethod(){
        
    }
}
