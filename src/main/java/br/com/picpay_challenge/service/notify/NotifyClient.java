package br.com.picpay_challenge.service.notify;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "notifier", url = "https://util.devi.tools/api/v1")
public interface NotifyClient {

    @RequestMapping(path = "/notify", method = RequestMethod.POST)
    ResponseEntity<Void> notifyClient();
}
