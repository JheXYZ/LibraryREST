package com.JheX.Library.library.PublishingHouse;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name = "publishing_houses")
public class PublishingHouse {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;

        @NotNull
        @NotEmpty
        String name;

        public PublishingHouse() {
        }

        public PublishingHouse(String name) {
                this.name = name;
        }

        public PublishingHouse(Long id, String name) {
                this.id = id;
                this.name = name;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        @Override
        public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                PublishingHouse publishingHouse = (PublishingHouse) o;
                return Objects.equals(id, publishingHouse.id) && Objects.equals(name, publishingHouse.name);
        }

        @Override
        public int hashCode() {
                return Objects.hash(id, name);
        }

        @Override
        public String toString() {
                return "Editorial{" +
                        "id=" + id +
                        ", name='" + name + '\'' +
                        '}';
        }
}
