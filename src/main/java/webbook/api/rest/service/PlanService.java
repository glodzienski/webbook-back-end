package webbook.api.rest.service;

import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webbook.api.model.entity.Plan;
import webbook.api.rest.repository.PlanRepository;
import webbook.api.core.helper.UUIDGeneratorHelper;

@Service
public class PlanService implements ApiCrudServiceContract<Plan> {
    @Autowired
    private PlanRepository repository;

    @Contract(pure = true)
    public PlanService(PlanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Plan store(Plan user) {
        user.setCode(UUIDGeneratorHelper.get());

        return repository.save(user);
    }

    @Override
    public Plan update(Plan currentPlan, Plan requestPlan) {
        currentPlan.setName(requestPlan.getName());
        currentPlan.setValue(requestPlan.getValue());

        return repository.save(currentPlan);
    }

    @Override
    public Plan getById(int id) {
        return null;
    }

    @Override
    public Plan getByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public Iterable<Plan> list() {
        return repository.findAll();
    }

    @Override
    public void destroy(Plan user) {
        repository.delete(user);
    }
}
