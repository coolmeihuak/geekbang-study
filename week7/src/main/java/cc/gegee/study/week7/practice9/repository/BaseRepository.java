package cc.gegee.study.week7.practice9.repository;


import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<Jpa, IDType> extends JpaRepository<Jpa, IDType> {

}
