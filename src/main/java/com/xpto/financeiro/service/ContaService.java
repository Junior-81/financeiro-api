package com.xpto.financeiro.service;

import com.xpto.financeiro.dto.ContaDTO;
import com.xpto.financeiro.entity.Cliente;
import com.xpto.financeiro.entity.Conta;
import com.xpto.financeiro.exception.BusinessException;
import com.xpto.financeiro.repository.ClienteRepository;
import com.xpto.financeiro.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;

    public Conta criarConta(ContaDTO contaDTO) {
        // Verificar se cliente existe
        Cliente cliente = clienteRepository.findById(contaDTO.getClienteId())
                .orElseThrow(() -> new BusinessException("Cliente não encontrado"));
        
        // Verificar se já existe conta com mesmo número e agência
        if (contaRepository.findByNumeroAndAgencia(contaDTO.getNumero(), contaDTO.getAgencia()).isPresent()) {
            throw new BusinessException("Já existe conta com este número e agência");
        }
        
        Conta conta = new Conta();
        conta.setNumero(contaDTO.getNumero());
        conta.setAgencia(contaDTO.getAgencia());
        conta.setInstituicaoFinanceira(contaDTO.getInstituicaoFinanceira());
        conta.setTipoConta(contaDTO.getTipoConta());
        conta.setSaldoAtual(contaDTO.getSaldoAtual() != null ? contaDTO.getSaldoAtual() : 0.0);
        conta.setCliente(cliente);
        
        return contaRepository.save(conta);
    }

    public List<Conta> listarContas() {
        return contaRepository.findByAtivoTrue();
    }
    
    public List<Conta> listarContasPorCliente(Long clienteId) {
        return contaRepository.findByClienteIdAndAtivoTrue(clienteId);
    }

    public Optional<Conta> buscarContaPorId(Long id) {
        return contaRepository.findById(id)
                .filter(Conta::getAtivo);
    }

    public Conta atualizarConta(Long id, ContaDTO contaDTO) {
        Conta contaExistente = buscarContaPorId(id)
                .orElseThrow(() -> new BusinessException("Conta não encontrada"));
        
        // Verificar se tem movimentações antes de alterar dados críticos
        if (!contaExistente.getMovimentacoes().isEmpty()) {
            throw new BusinessException("Não é possível alterar conta com movimentações existentes");
        }
        
        contaExistente.setInstituicaoFinanceira(contaDTO.getInstituicaoFinanceira());
        contaExistente.setTipoConta(contaDTO.getTipoConta());
        
        return contaRepository.save(contaExistente);
    }

    public void deletarConta(Long id) {
        Conta conta = buscarContaPorId(id)
                .orElseThrow(() -> new BusinessException("Conta não encontrada"));
        
        // Exclusão lógica
        conta.setAtivo(false);
        contaRepository.save(conta);
    }
    
    public void atualizarSaldo(Long contaId, Double novoSaldo) {
        Conta conta = buscarContaPorId(contaId)
                .orElseThrow(() -> new BusinessException("Conta não encontrada"));
        
        conta.setSaldoAtual(novoSaldo);
        contaRepository.save(conta);
    }
}
