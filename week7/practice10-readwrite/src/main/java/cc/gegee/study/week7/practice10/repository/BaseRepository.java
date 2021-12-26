package cc.gegee.study.week7.practice10.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<Jpa, IDType> extends JpaRepository<Jpa, IDType> {

}
