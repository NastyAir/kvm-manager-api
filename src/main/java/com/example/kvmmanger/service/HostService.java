package com.example.kvmmanger.service;

import com.example.kvmmanger.common.Result;
import com.example.kvmmanger.common.contant.RetCode;
import com.example.kvmmanger.common.kvm.KvmConnectionProvider;
import com.example.kvmmanger.common.kvm.KvmMultipleConnFactory;
import com.example.kvmmanger.common.util.RetResponse;
import com.example.kvmmanger.entity.Host;
import com.example.kvmmanger.repository.HostRepository;
import org.apache.commons.lang3.StringUtils;
import org.libvirt.Connect;
import org.libvirt.LibvirtException;
import org.libvirt.NodeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

import static com.example.kvmmanger.common.util.BeanUpdateUtils.getNullPropertyNames;

@Service
public class HostService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostService.class);

    private final HostRepository hostRepository;

    @Autowired
    public HostService(HostRepository hostRepository) {
        this.hostRepository = hostRepository;
    }

    public Result list(String hostName, String order, String orderColumn, String currentPage, String pageSize) {
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
        return  RetResponse.success(page);
    }

    public Result add(Host host) {
        host.setId(null);
        Host savedHost = hostRepository.save(host);
        return RetResponse.success(savedHost);
    }

    public Result update(Host host, @NotBlank Integer id) {
        host.setId(null);
        Host target = hostRepository.getOne(id);
        BeanUtils.copyProperties(host,target,getNullPropertyNames(host));
        Host savedHost = hostRepository.save(target);
        return RetResponse.success(savedHost);
    }

    public Result del(Integer id) {
        Host target = hostRepository.getOne(id);
        if (target==null){
            return RetResponse.make(RetCode.RECORD_NOT_FOUND);
        }
        hostRepository.delete(target);
        return RetResponse.success();
    }

    public Result get(Integer id) throws LibvirtException {
        Host target = hostRepository.getOne(id);
        KvmConnectionProvider connectionProvider = KvmMultipleConnFactory.getKvmConnect(target.getIp());
        Connect connect = connectionProvider.getConnection();
        NodeInfo nodeInfo = connect.nodeInfo();
        connectionProvider.returnConnection(connect);
        return RetResponse.success(nodeInfo);
    }
}
