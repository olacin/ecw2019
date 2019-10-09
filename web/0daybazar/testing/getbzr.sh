#!/bin/bash

cookie='session=.eJwdj01vgzAQRP9KtWcOCR9SitQbBHHYtVIZ0HKjQCnGdqpGEeAo_71ur280bzQPsFfbj5A-4OUDUqCinjnEHRs8kMKNCt5bhTEbDtnlK7n8yLJfUS4RyvOX57vIplVIPbO8hKSWlcMyas27pkwvrDgiNx2EXDZsSu_NQ1FgQrKMSVWu_WPqkrRZFYumnn1_x_-sP6Kadsz8pqGFZbWxqw01Vdw26ERRvsEzgPtt_LGd8QfA9t8DBNANZraQfnb6NgagOztBau9aBzAPkCan1-cvzSxQ3A.XZ47yA.rkNAwGU9kNtasninZPYoL3H8Ies'
base_url='https://web_0daybazar.challenge-ecw.fr'
save_path='/root/Documents/ecw2019/web/0daybazar/output/'

for f in $(find . -maxdepth 10 -type f | grep -v 'getbzr.sh') ; do
	url_end=$(ls $f | cut -c2-)
	full_url=$base_url$url_end
	printf "%s\n" $full_url
	curl --cookie $cookie $full_url > $save_path$filename
	filename=$(echo $full_url | rev | cut -d'/' -f1 | rev)
	printf "Saved file to %s\n" $save_path$filename
done
