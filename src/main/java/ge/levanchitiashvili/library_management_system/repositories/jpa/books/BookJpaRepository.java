package ge.levanchitiashvili.library_management_system.repositories.jpa.books;

import ge.levanchitiashvili.library_management_system.config.BaseRepository;
import ge.levanchitiashvili.library_management_system.models.books.Book;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookJpaRepository extends BaseRepository<Book,Long> {


    Optional<Book>  findBookByTitleAndActiveTrue(String title);


    Optional<Book>  findBookByIsbnAndActiveTrue(String isbn);

}
