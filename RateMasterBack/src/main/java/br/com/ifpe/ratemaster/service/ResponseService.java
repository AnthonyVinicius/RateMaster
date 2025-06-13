package br.com.ifpe.ratemaster.service;

import br.com.ifpe.ratemaster.entity.ResponseModel;
import br.com.ifpe.ratemaster.repository.ResponseRepository;
import br.com.ifpe.ratemaster.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseService {

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public List<ResponseModel> getAllResponses() {
        return responseRepository.findAll();
    }

    public ResponseModel saveResponse(ResponseModel responseModel) {
        return responseRepository.save(responseModel);
    }

}
