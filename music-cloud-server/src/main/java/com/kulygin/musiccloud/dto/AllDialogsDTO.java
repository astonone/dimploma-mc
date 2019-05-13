package com.kulygin.musiccloud.dto;

import com.kulygin.musiccloud.domain.Dialog;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static org.hibernate.validator.internal.util.CollectionHelper.newHashSet;

@Getter
@Setter
public class AllDialogsDTO {
    private Set<DialogDTO> dialogs;
    private int countAll;

    public AllDialogsDTO() {
    }

    public AllDialogsDTO(Collection<Dialog> dbModel, int count) {
        if (dbModel == null) {
            return;
        }
        this.dialogs = ofNullable(dbModel).orElse(newHashSet()).stream()
                .map(DialogDTO::new)
                .collect(Collectors.toSet());
        this.countAll = count;
    }
}
