package com.example.kvmmanger.service;

import com.example.kvmmanger.common.RestMessage;
import com.example.kvmmanger.common.util.RestMessageUtil;
import com.example.kvmmanger.entity.Host;
import com.example.kvmmanger.repository.HostRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class HostService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostService.class);

    private final HostRepository hostRepository;

    @Autowired
    public HostService(HostRepository hostRepository) {
        this.hostRepository = hostRepository;
    }

    public RestMessage list(String hostName, String order, String orderColumn, String currentPage, String pageSize) {
        //设置排序
        Sort.Direction direction = "ASC".equals(order.toUpperCase()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        //设置分页
        Pageable pageable = PageRequest.of(
                Integer.valueOf(currentPage),
                Integer.valueOf(pageSize),
                Sort.by(direction, orderColumn));
        // 设置查询条件
        Specification<Host> specification =  (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotEmpty(hostName)) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + hostName + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        Page page = hostRepository.findAll(specification, pageable);
        return RestMessageUtil.success(page);
    }
}
