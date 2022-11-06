package com.harera.dwaaserver.mapper

import com.harera.dwaaserver.dto.model.Packaging
import com.harera.dwaaserver.entity.PackagingEntity

object PackagingMapper {

    @Throws(NullPointerException::class)
    fun map(packagingEntity: PackagingEntity): Packaging {
        return Packaging(
            packagingEntity.packagingId!!,
            packagingEntity.packagingType!!
        )
    }
}