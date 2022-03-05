package ru.consulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.consulting.dto.PositionDto;
import ru.consulting.service.PositionService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("position")
public class PositionController {
    private PositionService positionService;

    @Autowired
    public void setPositionService(PositionService positionService) {
        this.positionService = positionService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void saveOrUpdate(@RequestBody @Valid PositionDto positionDto) {
        positionService.saveNewOrUpdate(positionDto);
    }

    @DeleteMapping
    public void deletesByTitles(@RequestBody @NotEmpty String[] titles) {
        positionService.removes(titles);
    }

    @RequestMapping("{title}")
    public Iterable<PositionDto> showAllByFiltering(@RequestParam(required = false) Boolean idAscending,
                                                    @RequestParam(required = false) Boolean idDescending,
                                                    @PathVariable(required = false) boolean title) {
        return positionService.getAll(idAscending, idDescending, title);
    }
}
