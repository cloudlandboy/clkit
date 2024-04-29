package cn.clboy.clkit.os.service;

import cn.clboy.clkit.os.vo.PidInfoVO;

import java.util.List;

/**
 * 进程服务
 *
 * @author clboy
 * @date 2024/04/22 11:34:50
 */
public interface ProcessService {

    /**
     * 获取信息
     *
     * @param type  类型
     * @param value 值
     */
    List<PidInfoVO> getInfo(String type, String value);
}
