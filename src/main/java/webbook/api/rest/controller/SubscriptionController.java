package webbook.api.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import webbook.api.core.helper.AuthHelper;
import webbook.api.model.entity.Address;
import webbook.api.model.entity.Plan;
import webbook.api.model.entity.Subscription;
import webbook.api.rest.service.AddressService;
import webbook.api.rest.service.PlanService;
import webbook.api.rest.service.SubscriptionService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController implements ApiCrudControllerContract<Subscription> {

    @Autowired
    private SubscriptionService service;

    @Autowired
    private PlanService planService;

    @Autowired
    private AddressService addressService;

    @Override
    public Subscription store(@Valid Subscription subscription) {
        Plan plan = planService.getByCode(subscription.getPlan().getCode());
        if (plan == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Plano inválido.");
        }

        Address address = addressService.getByCode(subscription.getAddress().getCode());
        if (address == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereço inválido.");
        }

        subscription.setUser(AuthHelper.user());
        subscription.setPlan(plan);
        subscription.setAddress(address);

        return service.store(subscription);
    }

    @Override
    public Subscription update(String code, @Valid Subscription subscription) {
        Subscription currentSubscription = service.getByCode(code);
        if (currentSubscription == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Assinatura não encontrada.");
        }

        return service.update(currentSubscription, subscription);
    }

    @Override
    public Subscription getByCode(@PathVariable String code) {
        Subscription subscription = service.getByCode(code);
        if (subscription == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Assinatura não encontrada.");
        }

        return subscription;
    }

    @Override
    public Iterable<Subscription> list() {
        return service.list();
    }

    @Override
    public void destroy(String code) {
        Subscription subscription = service.getByCode(code);
        if (subscription == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Assinatura não encontrada.");
        }

        service.destroy(subscription);
    }
}
