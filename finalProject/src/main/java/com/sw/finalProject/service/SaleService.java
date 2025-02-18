package com.sw.finalProject.service;

import com.sw.finalProject.dto.SaleForm;
import com.sw.finalProject.entity.Sale;
import com.sw.finalProject.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service //전체 구조에서 서비스 역할을 하는 객체를 뜻함.
//@Component (기능은 같음, 일반적인 객체로 만듦)
public class SaleService {
    //서비스를 통해 요청하기 때문에 repository를 가지고 있어야 함.
    @Autowired
    private SaleRepository saleRepository;

    public List<Sale> getAll() {
        List<Sale> lists = (List<Sale>) saleRepository.findAll();

        return lists;
    }

    //json 형태가 아니기 때문에 @RequestBody 있으면 안 됨.
    public Sale create(SaleForm dto) {
        if(dto.getId() != null) { //dto 에러 : id 값이 있으면 안 됨.
            return null;
        }

        //DTO(SaleForm 식판) => 엔티티(Sale)
        Sale entity = dto.toEntity();

        Sale savedSale = saleRepository.save(entity);

        return savedSale;
    }

    public Sale getOne(Long id) {
        Sale sale = saleRepository.findById(id).orElse(null);

        return sale;
    }

    public Sale update(Long id, SaleForm dto) {
        // 1. DTO -> Entity
        Sale entity = dto.toEntity();

        // 2. 타깃 조회하기 -> 수정할 내용이 있는지 검사(ex. 5번 내용이 있는지 검사)
        Sale target = saleRepository.findById(id).orElse(null);

        // 3. 잘못된 요청 처리하기 -> 타깃 조회했는데 없을 시 없다고 응답
        if(target == null) { //수정하려는 id가 없을 때
            return null;
        }

        // 4. 업데이트 및 정상적인 응답
        // 진짜 수정 내용은 entity에 들어 있음. but 내가 수정하려는 내용의 id 번호가 없음.
        entity.setId(id); //저장하기 전에 id를 먼저 찾아줘야 수정이 가능함.

        // 수정하지 않는 부분을 entity에 복사
        if(entity.getTitle() == null) entity.setTitle(target.getTitle()); //content만 수정할 경우 title은 가져옴.
        if(entity.getContent() == null) entity.setContent(target.getContent()); //title만 수정할 경우 content는 가져옴.

        Sale updated = saleRepository.save(entity);

        return updated;
    }

    public Sale deleteOne(Long id) {
        // 삭제하려는 내용의 id 찾기
        Sale target = saleRepository.findById(id).orElse(null);

        if(target == null) { //삭제하려는 내용이 없을 때
            return null;
        } else { //삭제하려는 내용이 있을 때
            saleRepository.deleteById(id);
            return target;
        }
    }

    @Transactional
    public List<Sale> createSales(List<SaleForm> dtos) {
        List<Sale> savedList = new ArrayList<Sale>(); //3건을 저장할 변수

        // 1. dto 3건을 entity로 변환
        for(int i = 0; i < dtos.size(); i++) {
            Sale aa = dtos.get(i).toEntity();

            //강제적으로 에러 발생시키기
            if(i == 2) {
                // -1L(Long) 아이디를 찾는데 없으면 orElseThrow를 통해 에러 발생 시킴 -> 프로그램 중지
                // 에러 발생 이유를 직접 작성할 수 있음.
                saleRepository.findById(-1L).orElseThrow(() -> new IllegalArgumentException("강제 발생"));
            }

            // 2. DB 저장
            Sale saved =  saleRepository.save(aa);
            savedList.add(saved);
        }

        // 3. 결과(저장된 3건) 리턴
        return savedList;
    }
}
