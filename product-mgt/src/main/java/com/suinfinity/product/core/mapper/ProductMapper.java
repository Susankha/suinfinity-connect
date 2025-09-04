package com.suinfinity.product.core.mapper;

import com.suinfinity.product.core.dto.ProductDTO;
import com.suinfinity.product.core.dto.ProductResponseDTO;
import com.suinfinity.product.core.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = ComponentModel.SPRING)
public interface ProductMapper {

  ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

  Product toProduct(ProductDTO productDTO);

  ProductResponseDTO toProductResponseDTO(Product product);
}
