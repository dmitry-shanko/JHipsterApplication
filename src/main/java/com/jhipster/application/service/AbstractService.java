package com.jhipster.application.service;

import com.jhipster.application.context.Context;
import com.jhipster.application.context.ContextHolder;
import com.jhipster.application.context.status.ErrorStatus;
import com.jhipster.application.context.status.ErrorStatusCode;
import com.jhipster.application.domain.BaseEntity;
import com.jhipster.application.repository.BaseEntityRepository;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

/**
 * Created by user on 31.05.2015.
 */
public abstract class AbstractService<R extends BaseEntityRepository<E, ID>, E extends BaseEntity<E, ID>, ID extends Serializable> {

    protected final Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Inject
    private ContextHolder contextHolder;

    @Inject
    private R repository;

    protected R getRepository() {
        return repository;
    }

    @Transactional(readOnly = true)
    public E findById(ID id) {
        return repository.findOne(id);
    }

    @Transactional(readOnly = true)
    public List<E> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<E> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional
    public E save(E entity) {
        try {
            E result = repository.save(entity);
            return result;
        } catch(Exception e) {
            logger.error("Can't update {}", e, entity);
            addError(ErrorStatusCode.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    @Transactional
    public void delete(ID id) {
        repository.delete(id);
    }

    protected Context getCurrentContext() {
        return contextHolder.getCurrentContext();
    }

    protected void addError(ErrorStatusCode errorStatusCode) {
        getCurrentContext().addStatus(new ErrorStatus(errorStatusCode));
    }

}
