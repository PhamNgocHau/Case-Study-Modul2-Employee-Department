package com.codegym.employe.service;

public interface GeneralService<D> {
    Iterable<D> findAllService();

    D findByIdService(Long id);

    void saveService(D d);

    void removeService(Long id);

}
