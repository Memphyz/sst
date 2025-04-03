package com.sst.controller.ppra;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(description = "A PPRA CRUD", name = "PPRA")
@RequestMapping("ppra")
public class PPRAController {

}
