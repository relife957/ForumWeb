package com.wangyi.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Cache(usage =CacheConcurrencyStrategy.READ_WRITE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "post_type",discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("2")
public class MainPost extends Post {

}
