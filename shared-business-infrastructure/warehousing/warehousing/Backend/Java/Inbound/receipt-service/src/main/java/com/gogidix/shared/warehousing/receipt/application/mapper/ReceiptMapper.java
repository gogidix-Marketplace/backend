package com.gogidix.shared.warehousing.receipt.application.mapper;

import com.gogidix.shared.warehousing.receipt.application.command.CreateReceiptCommand;
import com.gogidix.shared.warehousing.receipt.application.command.UpdateReceiptCommand;
import com.gogidix.shared.warehousing.receipt.application.dto.ReceiptDTO;
import com.gogidix.shared.warehousing.receipt.domain.entity.Receipt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ReceiptMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "receiptNumber", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "receiptDate", ignore = true)
    Receipt toEntity(CreateReceiptCommand command);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget Receipt receipt, UpdateReceiptCommand command);

    ReceiptDTO toDTO(Receipt receipt);
    List<ReceiptDTO> toDTOList(List<Receipt> receipts);
}
