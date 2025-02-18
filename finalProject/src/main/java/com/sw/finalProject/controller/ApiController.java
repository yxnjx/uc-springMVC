package com.sw.finalProject.controller;

import com.sw.finalProject.dto.SaleForm;
import com.sw.finalProject.entity.Sale;
import com.sw.finalProject.service.SaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ApiController {
    @Autowired
    SaleService saleService;

    //전체 목록(응답코드 조절)
    @GetMapping("/api/sales")
    //응답 코드 내 맘대로 조절하기 위해 ResponseEntity 사용
    public ResponseEntity<List<Sale>> getAllSales2() {
        //DB에 있는 모든 sale 가져오기
        List<Sale> allEntityList = saleService.getAll();

        return ResponseEntity.status(HttpStatus.OK).body(allEntityList);
        //상태코드는 OK
        //ResponseEntity의 body 안에 allEntityList가 들어감.
        //상태코드 내가 직접 선택할 수 있음.
    }

    // 게시판 글 등록(응답코드 조절)
    @PostMapping("/api/sales") //식판(DTO)을 쓰는 방법
    public ResponseEntity<Sale> create(@RequestBody SaleForm dto) {
        Sale savedSale = saleService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedSale);
    }

    // id 값에 맞는 게시판
    @GetMapping("/api/sales/{id}")
    public ResponseEntity<Sale> getOne(@PathVariable Long id) {
        Sale aaa = saleService.getOne(id);

        if(aaa == null) { //id가 없는 것 요청한 경우
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(aaa);
        }
    }

    // 수정 요청(수정된 내용 다시 보내줌)
    @PatchMapping("/api/sales/{id}")
    public ResponseEntity<Sale> update(@PathVariable Long id, @RequestBody SaleForm dto) { // json 데이터로 받을 때는 @RequestBody 사용
        Sale updatedSale = saleService.update(id, dto);

        if(updatedSale == null) { //id가 없는 것을 수정 요청했을 때
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(updatedSale);
        }
    }

    // 삭제 요청
    @DeleteMapping("/api/sales/{id}")
    public ResponseEntity<Sale> deleteOne(@PathVariable Long id) {
        Sale deleted = saleService.deleteOne(id);

        if(deleted == null) { //id가 없는 것을 수정 요청했을 때
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(deleted);
        }
    }

    //트랜잭션 테스트
    @PostMapping("/api/tr-test")
    public ResponseEntity<List<Sale>> trTest(@RequestBody List<SaleForm> dtos) { //3건이 dtos에 저장
        //여러 데이터를 한 번에 받음(데이터 3건을 List 형태로 저장)
        List<Sale> lists = saleService.createSales(dtos);

        return ResponseEntity.status(HttpStatus.CREATED).body(lists);
    }
}
