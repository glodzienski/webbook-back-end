package webbook.api.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import webbook.api.model.entity.BookInfo;

@Repository
public interface BookInfoRepository extends JpaRepository<BookInfo, String> {
}
