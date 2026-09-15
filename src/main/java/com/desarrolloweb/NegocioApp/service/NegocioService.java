package com.desarrolloweb.NegocioApp.service;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.desarrolloweb.NegocioApp.repository.NegocioRepository;
@Service
public class NegocioService {
    private NegocioRepository negocioRepository;
    @Autowired
    public NegocioService(negocioRepository negocioRepository){
        this.negocioRepocitory = negocioRepository;
    }  
}
