package webbook.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webbook.api.model.BookCategory;
import webbook.api.repository.BookCategoryRepository;
import webbook.api.helper.UUIDGeneratorHelper;

@Service
public class BookCategoryService implements ApiCrudServiceContract<BookCategory> {
    @Autowired
    private BookCategoryRepository repository;

    public BookCategoryService(BookCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public BookCategory store(BookCategory bookCategory) {
        bookCategory.setCode(UUIDGeneratorHelper.get());

        return repository.save(bookCategory);
    }

    @Override
    public BookCategory update(BookCategory currentBookCategory, BookCategory requestBookCategory) {
        currentBookCategory.setName(requestBookCategory.getName());
        currentBookCategory.setDescription(requestBookCategory.getDescription());

        return repository.save(currentBookCategory);
    }

    @Override
    public BookCategory getById(int id) {
        return null;
    }

    @Override
    public BookCategory getByCode(String code) {
        return repository.findByCode(code);
    }

    @Override
    public Iterable<BookCategory> list() {
        return repository.findAll();
    }

    @Override
    public void destroy(BookCategory bookCategory) {
        repository.delete(bookCategory);
    }
}
