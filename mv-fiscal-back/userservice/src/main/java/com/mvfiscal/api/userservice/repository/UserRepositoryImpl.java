package com.mvfiscal.api.userservice.repository;

import com.mvfiscal.api.userservice.dto.UserDTO;
import com.mvfiscal.api.userservice.model.User;
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
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<UserDTO> findPageByFilter(Long id, String name, String email, Date createdDate, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserDTO> cq = cb.createQuery(UserDTO.class);
        Root<User> root = cq.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (nonNull(id)) {
            predicates.add(cb.equal(root.get("id"), id));
        }

        if (nonNull(name) && !name.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
        }

        if (nonNull(email) && !email.isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
        }

        if (nonNull(createdDate)) {
            Expression<String> createdDateExpr = cb.function("TO_CHAR", String.class, root.get("createdDate"), cb.literal("dd-MM-yyyy"));
            String formatted = new SimpleDateFormat("dd-MM-yyyy").format(createdDate);
            predicates.add(cb.equal(createdDateExpr, formatted));
        }

        cq.select(cb.construct(
                UserDTO.class,
                root.get("id"),
                root.get("name"),
                root.get("email"),
                root.get("createdDate")
        )).where(predicates.toArray(new Predicate[0]));

        TypedQuery<UserDTO> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<User> countRoot = countQuery.from(User.class);
        countQuery.select(cb.count(countRoot)).where(predicates.toArray(new Predicate[0]));
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(query.getResultList(), pageable, total);
    }
}