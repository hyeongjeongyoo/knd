package cms.board.repository;

import cms.board.domain.BbsArticleCategoryDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BbsArticleCategoryRepository
        extends JpaRepository<BbsArticleCategoryDomain, BbsArticleCategoryDomain.BbsArticleCategoryId> {

    /**
     * 게시글 ID로 카테고리 연결 정보 삭제
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM BbsArticleCategoryDomain ac WHERE ac.article.nttId = :nttId")
    void deleteByArticleNttId(@Param("nttId") Long nttId);

    /**
     * 게시글 ID로 카테고리 연결 정보 조회
     */
    @Query("SELECT ac FROM BbsArticleCategoryDomain ac WHERE ac.article.nttId = :nttId")
    List<BbsArticleCategoryDomain> findByArticleNttId(@Param("nttId") Long nttId);

    /**
     * 카테고리 ID로 연결된 게시글 조회
     */
    @Query("SELECT ac FROM BbsArticleCategoryDomain ac WHERE ac.category.categoryId = :categoryId")
    List<BbsArticleCategoryDomain> findByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 게시글 ID와 카테고리 ID로 연결 정보 조회
     */
    @Query("SELECT ac FROM BbsArticleCategoryDomain ac WHERE ac.article.nttId = :nttId AND ac.category.categoryId = :categoryId")
    BbsArticleCategoryDomain findByArticleNttIdAndCategoryId(@Param("nttId") Long nttId,
            @Param("categoryId") Long categoryId);
}