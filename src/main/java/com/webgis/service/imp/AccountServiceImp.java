package com.webgis.service.imp;

import com.webgis.mysql.entity.AccountDO;
import com.webgis.mysql.mapper.AccountMapper;
import com.webgis.service.AccountService;
import com.webgis.web.BaseResult;
import com.webgis.web.dto.WebAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Justin on 2017/3/8.
 * 用户相关服务实现
 */

@Service
public class AccountServiceImp implements AccountService {

    @Autowired
    private AccountMapper accountMapper;


    /**
     * 用户注册
     * @param webAccount
     * @return
     */
    @Override
    public BaseResult<Object> register(WebAccount webAccount) {

        if (accountMapper.getAccountByUsername(webAccount.getUsername()) != null) {
            return new BaseResult<>(500, "用户已经存在");
        }
        accountMapper.insert(new AccountDO(webAccount));
        return new BaseResult<>();
    }

    /**
     * 更新用户信息
     * @param webAccount
     * @return
     */
    @Override
    public BaseResult<Object> update(WebAccount webAccount) {
        if (accountMapper.getAccountByUsername(webAccount.getUsername()) == null) {
            return new BaseResult<>(500, "该用户不存在");
        }
        accountMapper.update(new AccountDO(webAccount));
        return new BaseResult<>();
    }

    /**
     * 删除用户
     * @param userName
     * @return
     */
    @Override
    public BaseResult<Object> deleteAccount(String userName) {
        if (accountMapper.getAccountByUsername(userName) == null) {
            return new BaseResult<>(500, "该用户不存在");
        }
        accountMapper.deleteAccount(userName);
        return new BaseResult<>();
    }


}