package br.com.picpay_challenge.service.authorize;

import br.com.picpay_challenge.dto.AuthorizeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "authorizer", url = "https://util.devi.tools/api/v2")
public interface AuthorizeClient {

    @RequestMapping(method = RequestMethod.GET, value = "/authorize")
    AuthorizeDTO getAuthorize();
}
