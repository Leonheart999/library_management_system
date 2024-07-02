package ge.levanchitiashvili.library_management_system.repositories.jpa.books;

import ge.levanchitiashvili.library_management_system.config.BaseRepository;
import ge.levanchitiashvili.library_management_system.models.books.Book;
import org.springframework.stereotype.Repository;

@Repository
public interface BookJpaRepository extends BaseRepository<Book,Long> {
    Book findBookByTitleAndActiveTrue(String title);


    Book findBookByIsbnAndActiveTrue(String isbn);

}
