package com.mindata.blockchain.core.service;

import cn.hutool.core.bean.BeanUtil;
import com.mindata.blockchain.block.Instruction;
import com.mindata.blockchain.common.CommonUtil;
import com.mindata.blockchain.common.Sha256;
import com.mindata.blockchain.common.TrustSDK;
import com.mindata.blockchain.common.exception.TrustSDKException;
import com.mindata.blockchain.core.requestbody.InstructionBody;
import org.springframework.stereotype.Service;

/**
 * 一条指令的service
 *
 * @author wuweifeng wrote on 2018/3/7.
 */
@Service
public class InstructionService {
    /**
     * 校验公私钥是不是一对
     *
     * @param instructionBody
     *         instructionBody
     * @return boolean
     * @throws TrustSDKException
     *         TrustSDKException
     */
    public boolean checkKeyPair(InstructionBody instructionBody) throws TrustSDKException {
        return TrustSDK.checkPairKey(instructionBody.getPrivateKey(), instructionBody.getPublicKey());
    }

    /**
     * 根据传来的body构建一条指令
     *
     * @param instructionBody
     *         body
     * @return Instruction
     */
    public Instruction build(InstructionBody instructionBody) throws Exception {
        Instruction instruction = new Instruction();
        BeanUtil.copyProperties(instructionBody, instruction);
        instruction.setTimeStamp(CommonUtil.getNow());
        String buildStr = instructionBody.getOperation() + instructionBody.getTable() + instructionBody.getJson();
        //设置签名，供其他人验证
        instruction.setSign(TrustSDK.signString(instructionBody.getPrivateKey(), buildStr));
        //设置hash，防止篡改
        instruction.setHash(Sha256.sha256(buildStr));

        return instruction;
    }

    public boolean checkSign(Instruction instruction) throws TrustSDKException {
        String buildStr = instruction.getOperation() +
                instruction.getTable() + instruction.getJson();
        return TrustSDK.verifyString(instruction.getPublicKey(), buildStr, instruction.getSign());
    }

    public boolean checkHash(Instruction instruction) {
        String buildStr = instruction.getOperation() +
                instruction.getTable() + instruction.getJson();
        return Sha256.sha256(buildStr).equals(instruction.getHash());
    }
}