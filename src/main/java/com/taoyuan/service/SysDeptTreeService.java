package com.taoyuan.service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.taoyuan.dao.SysDeptMapper;
import com.taoyuan.dto.DeptLevelDto;
import com.taoyuan.model.SysDept;
import com.taoyuan.utils.DeptLevelUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 部门列表树形结构服务类
 * @ProjectName permission
 * @ClassName SysDeptTreeService
 * @Date 2019/12/16 8:40
 * @Author taoyuan
 * @Version 1.0
 */
@Service
public class SysDeptTreeService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    public List<DeptLevelDto> deptTree () {
        // 获取所有部门信息
        List<SysDept> deptList = sysDeptMapper.getAllDept();
        List<DeptLevelDto> dtoList = Lists.newArrayList();
        for (SysDept dept : deptList) {
            DeptLevelDto dto = DeptLevelDto.adapt(dept);
            dtoList.add(dto);
        }
        return deptListToTree(dtoList);
    }

    /**
     * 递归展示部门列表
     * @param deptLevelList
     * @return
     */
    public List<DeptLevelDto> deptListToTree (List<DeptLevelDto> deptLevelList) {
        if (CollectionUtils.isEmpty(deptLevelList)) {
            return Lists.newArrayList();
        }

        // level -> [dept1,dept2]
        Multimap<String, DeptLevelDto> levelDeptMap = ArrayListMultimap.create();
        List<DeptLevelDto> rootList = Lists.newArrayList();

        for (DeptLevelDto dto : deptLevelList) {
            levelDeptMap.put(dto.getLevel(), dto);
            if (DeptLevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }

        transformDeptTree(rootList, DeptLevelUtil.ROOT, levelDeptMap);
        return rootList;
    }

    public void transformDeptTree (List<DeptLevelDto> deptList, String level,
                                   Multimap<String, DeptLevelDto> levelDeptMap) {
        // 对deptLevelList进行遍历
        for (int i = 0; i < deptList.size(); i ++) {
            // 获取每一层的部门信息
            DeptLevelDto dto = deptList.get(i);
            // 通过工具类计算出下一层部门信息的level
            String nextLevel = DeptLevelUtil.calculateLevel(level, dto.getId());
            // 在levelDeptMap中通过nextLevel获取到需要的部门信息，处理下一层信息
            List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>) levelDeptMap.get(nextLevel);
            // 针对tempDeptList进行非空判断，只有当它不为空的时候，我们才对其进行数据处理
            if (CollectionUtils.isNotEmpty(tempDeptList)) {
                // 根据seq进行部门排序
                Collections.sort(tempDeptList, deptComparator);
                // 设计下一层级部门
                dto.setDeptList(tempDeptList);
                // 进入到下一层级进行处理 递归
                transformDeptTree(tempDeptList, nextLevel, levelDeptMap);
            }
        }
    }

    // 定义了一个比较器
    // 根据Seq来进行排序
    public Comparator<DeptLevelDto> deptComparator = new Comparator<DeptLevelDto>() {
        @Override
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };


}
