package com.instana.test.captureparams;

import com.instana.sdk.annotation.Span;
import com.instana.sdk.support.SpanSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static java.lang.String.format;

@SpringBootApplication
public class Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @RestController
    @RequestMapping(path = "/api/**")
    public static class Api {

        @GetMapping
        public ResponseEntity<?> serve(final HttpServletRequest request,
                                       @RequestParam Map<String,String> requestParams) {
            doServe_0(request.getRequestURI(), requestParams);
            doServe_1(request.getRequestURI(), requestParams);
            doServe_2(request.getRequestURI(), requestParams);

            return ResponseEntity.ok().build();
        }

        @Span(value = "doServeVoidWithTags", captureArguments = true, captureReturn = true)
        private void doServe_0(String contextPath, Map<String, String> requestParams) {
            SpanSupport.annotate(Span.Type.INTERMEDIATE, "doServeVoidWithTags", "tags.my_key", "my_value");
        }

        @Span(value = "doServeVoid", captureArguments = true, captureReturn = true)
        private void doServe_1(String contextPath, Map<String, String> requestParams) {
            requestParams.entrySet().forEach(entry -> LOGGER.warn(format("Param: %s -> %s", entry.getKey(), entry.getValue())));
        }

        @Span(value = "doServeReturn", captureArguments = true, captureReturn = true)
        private String doServe_2(String contextPath, Map<String, String> requestParams) {
            return contextPath;
        }

    }

}
