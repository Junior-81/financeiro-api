package com.xpto.financeiro.service;

import com.xpto.financeiro.dto.MovimentacaoDTO;
import com.xpto.financeiro.entity.Conta;
import com.xpto.financeiro.entity.Movimentacao;
import com.xpto.financeiro.enums.TipoMovimentacao;
import com.xpto.financeiro.exception.BusinessException;
import com.xpto.financeiro.repository.ContaRepository;
import com.xpto.financeiro.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;
    
    @Autowired
    private ContaRepository contaRepository;
    
    @Autowired
    private ContaService contaService;

    public Movimentacao criarMovimentacao(MovimentacaoDTO movimentacaoDTO) {
        Conta conta = contaRepository.findById(movimentacaoDTO.getContaId())
                .orElseThrow(() -> new BusinessException("Conta não encontrada"));
        
        // Calcular saldos
        BigDecimal saldoAnterior = BigDecimal.valueOf(conta.getSaldoAtual());
        BigDecimal valorMovimentacao = movimentacaoDTO.getValor();
        BigDecimal saldoPosterior;
        
        if (movimentacaoDTO.getTipo() == TipoMovimentacao.CREDITO) {
            saldoPosterior = saldoAnterior.add(valorMovimentacao);
        } else {
            saldoPosterior = saldoAnterior.subtract(valorMovimentacao);
            
            // Verificar se há saldo suficiente
            if (saldoPosterior.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("Saldo insuficiente para a operação");
            }
        }
        
        // Criar movimentação
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setTipo(movimentacaoDTO.getTipo());
        movimentacao.setValor(valorMovimentacao);
        movimentacao.setDescricao(movimentacaoDTO.getDescricao());
        movimentacao.setSaldoAnterior(saldoAnterior);
        movimentacao.setSaldoPosterior(saldoPosterior);
        movimentacao.setConta(conta);
        movimentacao.setProcessado(true);
        
        // Salvar movimentação
        Movimentacao movimentacaoSalva = movimentacaoRepository.save(movimentacao);
        
        // Atualizar saldo da conta
        contaService.atualizarSaldo(conta.getId(), saldoPosterior.doubleValue());
        
        return movimentacaoSalva;
    }

    public List<Movimentacao> listarMovimentacoes() {
        return movimentacaoRepository.findAll();
    }
    
    public List<Movimentacao> listarMovimentacoesPorConta(Long contaId) {
        return movimentacaoRepository.findByContaIdOrderByDataMovimentacaoDesc(contaId);
    }
    
    public List<Movimentacao> listarMovimentacoesPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return movimentacaoRepository.findByPeriodo(dataInicio, dataFim);
    }
    
    public List<Movimentacao> listarMovimentacoesPorClienteEPeriodo(Long clienteId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return movimentacaoRepository.findByClienteAndPeriodo(clienteId, dataInicio, dataFim);
    }

    public Optional<Movimentacao> buscarMovimentacaoPorId(Long id) {
        return movimentacaoRepository.findById(id);
    }
    
    public Long contarMovimentacoesPorCliente(Long clienteId, TipoMovimentacao tipo) {
        return movimentacaoRepository.countByClienteAndTipo(clienteId, tipo);
    }
    
    public BigDecimal somarValorPorCliente(Long clienteId, TipoMovimentacao tipo) {
        return movimentacaoRepository.sumValorByClienteAndTipo(clienteId, tipo);
    }
    
    public Long contarMovimentacoesPorPeriodo(Long clienteId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return movimentacaoRepository.countMovimentacoesPorPeriodo(clienteId, dataInicio, dataFim);
    }
    
    // Método para calcular tarifa com base no número de movimentações
    public BigDecimal calcularTarifa(Long clienteId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        Long quantidadeMovimentacoes = contarMovimentacoesPorPeriodo(clienteId, dataInicio, dataFim);
        
        BigDecimal tarifa = BigDecimal.ZERO;
        
        if (quantidadeMovimentacoes <= 10) {
            tarifa = BigDecimal.valueOf(quantidadeMovimentacoes).multiply(BigDecimal.valueOf(1.00));
        } else if (quantidadeMovimentacoes <= 20) {
            // Primeiras 10 a R$ 1,00
            tarifa = tarifa.add(BigDecimal.valueOf(10).multiply(BigDecimal.valueOf(1.00)));
            // Restantes a R$ 0,75
            tarifa = tarifa.add(BigDecimal.valueOf(quantidadeMovimentacoes - 10).multiply(BigDecimal.valueOf(0.75)));
        } else {
            // Primeiras 10 a R$ 1,00
            tarifa = tarifa.add(BigDecimal.valueOf(10).multiply(BigDecimal.valueOf(1.00)));
            // Próximas 10 a R$ 0,75
            tarifa = tarifa.add(BigDecimal.valueOf(10).multiply(BigDecimal.valueOf(0.75)));
            // Restantes a R$ 0,50
            tarifa = tarifa.add(BigDecimal.valueOf(quantidadeMovimentacoes - 20).multiply(BigDecimal.valueOf(0.50)));
        }
        
        return tarifa;
    }
}
