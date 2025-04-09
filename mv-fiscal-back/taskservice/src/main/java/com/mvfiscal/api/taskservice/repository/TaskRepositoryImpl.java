package com.mvfiscal.api.taskservice.repository;

import com.mvfiscal.api.taskservice.dto.TaskDTO;
import com.mvfiscal.api.taskservice.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Objects.nonNull;

@Repository
public class TaskRepositoryImpl implements TaskRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<TaskDTO> findPageByFilter(Long id, String title, String description, String status,
                                          Date createdDate, Date limitDate, Long userId, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TaskDTO> cq = cb.createQuery(TaskDTO.class);
        Root<Task> root = cq.from(Task.class);

        List<Predicate> predicates = new ArrayList<>();

        if (nonNull(id)) {
            predicates.add(cb.equal(root.get("id"), id));
        }

        if (nonNull(title) && !title.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        if (nonNull(description) && !description.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
        }

        if (nonNull(status) && !status.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("status")), status.toLowerCase()));
        }

        if (nonNull(createdDate)) {
            Expression<String> createdDateExpr = cb.function("TO_CHAR", String.class, root.get("createdDate"), cb.literal("dd-MM-yyyy"));
            String formatted = new SimpleDateFormat("dd-MM-yyyy").format(createdDate);
            predicates.add(cb.equal(createdDateExpr, formatted));
        }

        if (nonNull(limitDate)) {
            Expression<String> limitDateExpr = cb.function("TO_CHAR", String.class, root.get("limitDate"), cb.literal("dd-MM-yyyy"));
            String formatted = new SimpleDateFormat("dd-MM-yyyy").format(limitDate);
            predicates.add(cb.equal(limitDateExpr, formatted));
        }

        if (nonNull(userId)) {
            predicates.add(cb.equal(root.get("userId"), userId));
        }

        cq.select(cb.construct(
                TaskDTO.class,
                root.get("id"),
                root.get("title"),
                root.get("description"),
                root.get("status"),
                root.get("createdDate"),
                root.get("limitDate"),
                root.get("userId")
        )).where(predicates.toArray(new Predicate[0]));

        TypedQuery<TaskDTO> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Task> countRoot = countQuery.from(Task.class);
        countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[0]));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(query.getResultList(), pageable, total);
    }
}