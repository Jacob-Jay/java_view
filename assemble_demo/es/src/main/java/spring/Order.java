package spring;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Dynamic;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Data
@Document(indexName = "order",createIndex = false,dynamic = Dynamic.FALSE)
public class Order implements Serializable {
    private static final long serialVersionUID = -6976701275420008569L;
    
    /**
     * id
     */
    @Id
//    @org.springframework.data.elasticsearch.annotations.Field((index = false,type = FieldType.Integer)
    private Long id;
    /**
     * 订单编号
     */
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String orderNo;

    /**
     * 订单状态0:创建;1:排队中;2:充电中;3:已完成4:已取消5已暂停
     */
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Integer)
    private Integer orderStatus=0;
    /**
     * 订单支付状态：0未支付；1已支付；2退款中 3部分退款 4全部退款
     */
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Integer)
    private Integer payStatus = 0;



    /**
     * 门店ID
     */
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Long)
    private Long storeId;
    /**
     * 门店信息
     */
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Text)
    private String storeName;
    /**
     * 下单时间
     */
    @JsonFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss",timezone="Asia/Shanghai")
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date,format = DateFormat.date_hour_minute_second)
    private LocalDateTime orderTime;
    /**
     * 支付时间
     */
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "uuuu-MM-dd'T'HH:mm:ss",timezone="Asia/Shanghai")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Date,format = DateFormat.date_hour_minute_second)
    private LocalDateTime payTime;

    /**
     * 总价格
     */
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Float)
    private BigDecimal price;

    /**
     * 退款金额
     */
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Float)
    private BigDecimal refundAmount;
    /**
     * 退款说明
     */
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Text)
    private String refundReason;
    /**
     * 取消原因：1、设备异常导致无法服务2、电量不足导致无法服务3、车辆充电口未开启4、其他原因
     */
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Text)
    private String cancelReason;

    /**
     * 0、免下单  1、常规订单  2、加急订单
     */
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Integer)
    private Integer orderCategory = 1;



    public static Order data(){
        Order data = new Order();
        data.setId(1L);
        data.setOrderNo("stml00001");
        data.setStoreId(1L);
        data.setStoreName("jq shop");
        data.setOrderTime(LocalDateTime.now());
        data.setPayTime(LocalDateTime.now());
        data.setPrice(new BigDecimal("2.2"));
        data.setRefundAmount(BigDecimal.ZERO);
        data.setRefundReason("");
        data.setCancelReason("");
        data.setOrderCategory(1);
        data.setOrderStatus(0);
        data.setPayStatus(0);
        return data;
    }


    public static Map<String,Object> mapData(Order data1)  {
        Map<String, Object> data = new HashMap<>();
        Field[] declaredFields = Order.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            String name = declaredField.getName();
            if (name.equals("serialVersionUID")) {
                continue;
            }
            try {
                data.put(name, declaredField.get(data1));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

}
