export class CreateFulfillmentCommand {
  orderId: string;
  tenantId: string;
  warehouseId: string;
  customerId: string;
  customerEmail: string;
  items: FulfillmentItemDto[];
  shippingAddress: AddressDto;
  billingAddress?: AddressDto;
  priority?: 'STANDARD' | 'EXPRESS' | 'PRIORITY' | 'SAME_DAY';
  notes?: string;
}

export class UpdateStatusCommand {
  fulfillmentId: string;
  tenantId: string;
  status: 'PENDING' | 'PROCESSING' | 'PICKED' | 'PACKED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED';
  notes?: string;
}

export class FulfillmentQueryDto {
  tenantId: string;
  warehouseId?: string;
  orderId?: string;
  status?: string;
  customerId?: string;
  fromDate?: Date;
  toDate?: Date;
  page?: number;
  limit?: number;
  sortBy?: string;
  sortOrder?: 'ASC' | 'DESC';
}

export class FulfillmentItemDto {
  sku: string;
  productName: string;
  quantity: number;
  unitPrice: number;
  weight?: number;
  dimensions?: {
    length: number;
    width: number;
    height: number;
  };
  isFragile?: boolean;
  requiresColdStorage?: boolean;
  location?: string;
}

export class AddressDto {
  name: string;
  company?: string;
  addressLine1: string;
  addressLine2?: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
  phone?: string;
}

export class FulfillmentOrderDto {
  id: string;
  orderId: string;
  tenantId: string;
  warehouseId: string;
  customerId: string;
  customerEmail: string;
  items: FulfillmentItemDto[];
  shippingAddress: AddressDto;
  billingAddress?: AddressDto;
  status: string;
  priority: string;
  assignedTo?: string;
  trackingNumber?: string;
  carrierId?: string;
  carrierName?: string;
  estimatedDelivery?: Date;
  actualDelivery?: Date;
  shippedAt?: Date;
  notes?: string;
  createdAt: Date;
  updatedAt: Date;
}

export class ShipmentManifestDto {
  fulfillmentId: string;
  orderId: string;
  tenantId: string;
  warehouseId: string;
  carrierId?: string;
  carrierName?: string;
  trackingNumber?: string;
  shippingLabel?: string;
  manifestDate: Date;
  packages: PackageManifestDto[];
  totalWeight: number;
  totalValue: number;
  status: string;
}

export class PackageManifestDto {
  packageId: string;
  weight: number;
  dimensions: {
    length: number;
    width: number;
    height: number;
  };
  items: {
    sku: string;
    name: string;
    quantity: number;
  }[];
}
