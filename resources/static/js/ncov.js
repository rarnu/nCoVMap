/**
 * 显示中国地图
 */
function showMap() {
    $.ajax({
        url: '/map',
        dataType: 'json',
        success: (res) => {
            let optionMap = {
                backgroundColor: '#FFFFFF',
                title: {
                    text: '全国疫情数据',
                    subtext: '',
                    x:'center'
                },
                tooltip: { trigger: 'item'},
                visualMap: {
                    show: true,
                    x: 'right',
                    y: 'center',
                    splitList: [
                        {start: 1000},
                        {start: 500, end: 999},
                        {start: 100, end: 499},
                        {start: 10, end: 99},
                        {start: 1, end: 9},
                        {start: 0, end: 0}
                    ],
                    color: [
                        '#7D0000',
                        '#D52F30',
                        '#F4664C',
                        '#FFA477',
                        '#FFD5C0',
                        '#FFF1D5'
                    ]
                },

                series: [{
                    name: '确诊人数',
                    type: 'map',
                    mapType: 'china',
                    roam: false,
                    label: {
                        normal: { show: true},
                        emphasis: { show: false}
                    },
                    data:res
                }]
            };
            let chart = echarts.init(document.getElementById('map'));
            chart.setOption(optionMap);
        }
    });
}

/**
 * 显示每日疫情
 */
function showDaily() {
    $.ajax({
        url: '/daily',
        dataType: 'json',
        success: (res) => {
            let optionMap = {
                tooltip: {trigger: 'axis' },
                legend: { data: ['确诊','疑似','死亡','治愈'] },
                xAxis: [{
                        type: 'category',
                        boundaryGap: false,
                        data: res.date
                    }],
                yAxis: [{type : 'value'}],
                series: [
                    {
                        name: '确诊',
                        type: 'line',
                        data: res.confirm,
                        color: '#D52F30'
                    },
                    {
                        name: '疑似',
                        type: 'line',
                        data: res.suspect,
                        color: '#FFA477'
                    },
                    {
                        name: '死亡',
                        type: 'line',
                        data: res.dead,
                        color: '#848586'
                    },
                    {
                        name: '治愈',
                        type: 'line',
                        data: res.heal,
                        color: '#64CC98'
                    }
                ]
            };
            let chart = echarts.init(document.getElementById('daily'));
            chart.setOption(optionMap);
        }
    });
}

function showDetail() {
    $.ajax({
        url: '/detail',
        dataType: 'json',
        success: (res) => {
            let str = "";
            $.each(res, (idx, item) => {
                str += `<li>`;
                str += `<div class="title"><div class="detailbar"><div class="area">${item.area}</div><div class="data">${item.confirm}</div><div class="data">${item.dead}</div><div class="data">${item.heal}</div></div></div>`;
                str += `<ul class='list-se'>`;
                str += `<div class="title"><div class="detailbar"><div class="area">${item.area}</div><div class="data">${item.confirm}</div><div class="data">${item.dead}</div><div class="data">${item.heal}</div></div></div>`;
                $.each(item.cities, (idx1, item1) => {
                    str += `<li><div class="detailbar"><div class="area">${item1.city}</div><div class="data">${item1.confirm}</div><div class="data">${item1.dead}</div><div class="data">${item1.heal}</div></div></li>`;
                });
                str += `</ul>`;
                str += `</li>`;
            });

            $("#detail_data")[0].innerHTML = str;

            jQuery(document).ready(function($)
            {
                $('.list ul li div').clickdown();
                $('.list-se div[class=title]').clickup();
                $('.list>ul>li').ad();
            });
        }
    });
}