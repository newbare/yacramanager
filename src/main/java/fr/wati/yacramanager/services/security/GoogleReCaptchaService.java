package fr.wati.yacramanager.services.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GoogleReCaptchaService implements InitializingBean, EnvironmentAware {

    private RestOperations restOperations;
    private String serviceUrl;
    private RelaxedPropertyResolver propertyResolver;

    @Override
    public void setEnvironment(Environment env) {
        this.propertyResolver = new RelaxedPropertyResolver(env, "google.recaptcha.");
    }

    public GoogleReCaptchaService() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new GoogleReCaptchaResponseMessageConverter());
        converters.add(new GoogleReCaptchaRequestMessageConverter());
        this.restOperations = new RestTemplate(converters);
    }

    public GoogleReCaptchaResponse validateCaptcha(String clientResponseToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(this.serviceUrl).queryParam("secret", propertyResolver.getProperty("secretKey")).queryParam("response",
                clientResponseToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        if (propertyResolver.getProperty("enabled", Boolean.class, true)) {
            HttpEntity<GoogleReCaptchaResponse> response = restOperations.exchange(builder.build().toUri(), HttpMethod.POST, null, GoogleReCaptchaResponse.class);
            return response.getBody();
        } else {
            GoogleReCaptchaResponse alwaysOKResponse = new GoogleReCaptchaResponse();
            alwaysOKResponse.setSuccess(true);
            return alwaysOKResponse;
        }
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public RestOperations getRestOperations() {
        return restOperations;
    }

    public static class GoogleReCaptchaRequest {
        private String secret;
        private String response;
        private String remoteip;

        public GoogleReCaptchaRequest(String secret, String response) {
            super();
            this.secret = secret;
            this.response = response;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }

        public String getRemoteip() {
            return remoteip;
        }

        public void setRemoteip(String remoteip) {
            this.remoteip = remoteip;
        }

    }

    public static class GoogleReCaptchaResponse {
        private boolean success;
        private List<String> errorCodes = new ArrayList<>();

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public List<String> getErrorCodes() {
            return errorCodes;
        }

        public void setErrorCodes(List<String> errorCodes) {
            this.errorCodes = errorCodes;
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.serviceUrl = propertyResolver.getProperty("url");
    }

    public static class GoogleReCaptchaResponseMessageConverter extends AbstractHttpMessageConverter<GoogleReCaptchaResponse> {

        @Override
        protected boolean supports(Class<?> clazz) {
            return clazz.isAssignableFrom(GoogleReCaptchaResponse.class);
        }

        @Override
        protected boolean canRead(MediaType mediaType) {
            if (mediaType == null) {
                return true;
            }
            return MediaType.APPLICATION_JSON.includes(mediaType);
        }

        @Override
        protected GoogleReCaptchaResponse readInternal(Class<? extends GoogleReCaptchaResponse> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {

            Map<String, ?> result = new ObjectMapper().readValue(IOUtils.toString(inputMessage.getBody()), HashMap.class);
            GoogleReCaptchaResponse response = new GoogleReCaptchaResponse();
            response.setSuccess((Boolean) result.get("success"));
            if (result.containsKey("error-codes")) {
                response.setErrorCodes((List<String>) result.get("error-codes"));
            } else {
                response.setErrorCodes(new ArrayList<String>());
            }
            return response;
        }

        @Override
        protected void writeInternal(GoogleReCaptchaResponse t, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
            System.out.println("");
        }

    }

    public static class GoogleReCaptchaRequestMessageConverter extends MappingJackson2HttpMessageConverter {

        @Override
        protected boolean supports(Class<?> clazz) {
            return clazz.isAssignableFrom(GoogleReCaptchaRequest.class);
        }

    }

}
