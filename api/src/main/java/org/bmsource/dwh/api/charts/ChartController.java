package org.bmsource.dwh.api.charts;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/charts")
public class ChartController {

  @GetMapping
  public String hello() {
    return "hello";
  }

}
