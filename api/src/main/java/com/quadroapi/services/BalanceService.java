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
      long createdAt = System.currentTimeMillis();
      this.balanceRepository.createBalance(team_id, tag_id, value, createdAt);
      return;
    } catch (Exception e) {
      System.out.println(e);
      throw new RuntimeException("Erro ao criar extrato");
    }
  }

  public Balance[] listBalance(int teamId, int page, int limit) {
    int offset = (page - 1) * limit;
    return this.balanceRepository.listBalance(teamId, offset, limit);
  }
}
