package com.xpto.financeiro.service;

import com.xpto.financeiro.dto.ClienteDTO;
import com.xpto.financeiro.entity.Cliente;
import com.xpto.financeiro.entity.PessoaFisica;
import com.xpto.financeiro.entity.PessoaJuridica;
import com.xpto.financeiro.enums.TipoPessoa;
import com.xpto.financeiro.exception.BusinessException;
import com.xpto.financeiro.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente criarCliente(ClienteDTO clienteDTO) {
        // Validar se já existe cliente com CPF/CNPJ
        validarDocumentoUnico(clienteDTO);
        
        Cliente cliente = converterDTOParaEntity(clienteDTO);
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findByAtivoTrue();
    }
    
    public Page<Cliente> listarClientesPaginados(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    public Optional<Cliente> buscarClientePorId(Long id) {
        return clienteRepository.findById(id)
                .filter(Cliente::getAtivo);
    }

    public Cliente atualizarCliente(Long id, ClienteDTO clienteDTO) {
        Cliente clienteExistente = buscarClientePorId(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado"));
        
        // Atualizar apenas campos permitidos
        clienteExistente.setNome(clienteDTO.getNome());
        clienteExistente.setEmail(clienteDTO.getEmail());
        clienteExistente.setTelefone(clienteDTO.getTelefone());
        
        return clienteRepository.save(clienteExistente);
    }

    public void deletarCliente(Long id) {
        Cliente cliente = buscarClientePorId(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado"));
        
        // Exclusão lógica
        cliente.setAtivo(false);
        clienteRepository.save(cliente);
    }
    
    public List<Cliente> buscarPorNome(String nome) {
        return clienteRepository.findByNomeContainingIgnoreCase(nome);
    }
    
    public Optional<Cliente> buscarPorCpf(String cpf) {
        return clienteRepository.findByCpf(cpf);
    }
    
    public Optional<Cliente> buscarPorCnpj(String cnpj) {
        return clienteRepository.findByCnpj(cnpj);
    }
    
    private void validarDocumentoUnico(ClienteDTO clienteDTO) {
        if (clienteDTO.getTipoPessoa() == TipoPessoa.FISICA) {
            if (clienteRepository.findByCpf(clienteDTO.getCpf()).isPresent()) {
                throw new BusinessException("Já existe cliente com este CPF");
            }
        } else {
            if (clienteRepository.findByCnpj(clienteDTO.getCnpj()).isPresent()) {
                throw new BusinessException("Já existe cliente com este CNPJ");
            }
        }
    }
    
    private Cliente converterDTOParaEntity(ClienteDTO dto) {
        Cliente cliente;
        
        if (dto.getTipoPessoa() == TipoPessoa.FISICA) {
            PessoaFisica pf = new PessoaFisica();
            pf.setCpf(dto.getCpf());
            pf.setRg(dto.getRg());
            if (dto.getDataNascimento() != null) {
                pf.setDataNascimento(LocalDate.parse(dto.getDataNascimento()));
            }
            cliente = pf;
        } else {
            PessoaJuridica pj = new PessoaJuridica();
            pj.setCnpj(dto.getCnpj());
            pj.setRazaoSocial(dto.getRazaoSocial());
            pj.setNomeFantasia(dto.getNomeFantasia());
            pj.setInscricaoEstadual(dto.getInscricaoEstadual());
            cliente = pj;
        }
        
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setSaldoInicial(dto.getSaldoInicial() != null ? dto.getSaldoInicial() : 0.0);
        
        return cliente;
    }
}