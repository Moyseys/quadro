package com.quadroapi.services;

import com.quadroapi.models.Balance;
import com.quadroapi.repositories.BalanceRepository;

import org.json.JSONObject;

import javax.management.RuntimeErrorException;

import org.json.JSONArray;

public class BalanceService {
  private BalanceRepository balanceRepository;

  public BalanceService(BalanceRepository balanceRepository) {
    this.balanceRepository = balanceRepository;
  }

  public void createBalance(int team_id, int tag_id, double value) {
    try {
      this.balanceRepository.createBalance(team_id, tag_id, value);
      return;
    } catch (Exception e) {
      throw new RuntimeException("Erro ao listar usuários");
    }
  }
}
