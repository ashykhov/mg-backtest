package com.shykhov.backtest.application.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BtRepo : JpaRepository<BtEntity, Long> {
}
