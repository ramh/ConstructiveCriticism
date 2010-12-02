#! /usr/bin/perl
use warnings;
use strict;
local $| = 1;
use WWW::Mechanize;
use HTTP::Response;



my $root = 'http://news.cnet.com/';
my $url = $root.'latest-news/?tag=hdr;snav';
my $top = WWW::Mechanize->new();
my $mech = WWW::Mechanize->new();
my $response;
my $post;
my $title;
my $body;
my $postID;
my $comment;
my $link;
my @comments;
my @responses;
my $set=0;

#$url="8301-17852_3-20022698-71.html";
$url="8301-30684_3-20024325-265.html";
$url=$ARGV[0] if (defined $ARGV[0]);

open OUT, ">>article.xml";
print OUT '<?xml version="1.0"?>';
print OUT "\n<posts>\n";



	$response = $mech->get($root.$url);
	$post = $response->decoded_content;

	$postID = $url;
	if($post =~ m/<h1>([^<]+)<\/h1>/s) {
		$_=$1;
		print "\t...$_\n";
		$_=~s/&/&amp;/g;
		$title=$_;
	}
	if($post =~ m/<div class="postBody" >(.+?)<\/div> <div class="editorBio">/s){
		$_=$1;
		$_=~s/<[^>]+?>//g;
		$_=~s/&/&amp;/g;
		$body=$_;
	}
	

	
	my $index=-1;
	@comments=();
	@responses=();
	do{
		$response = $mech->get($url);
		$post = $response->decoded_content;

		foreach $comment($post =~ m/<dl messageid="\d+"(.*?>.+?<\/dd>)/gs){
			if($comment =~ m/class="thread">.+?<dd id="body_\d+">(.+?)<\/dd>/s){
				$responses[$index]++;				
			} elsif($comment =~ m/<dd id="body_\d+">(.+?)<\/dd>/s){
				$_=$1;
				$_=~s/&/&amp;/g;				
				push (@comments,$_);
			 	push (@responses,0);
				$index++;
			}
		}

		$link=$mech->find_link( text => 'next');
		$url=$link->url() if (defined $link);
		
	}while(defined $link );

	print OUT "\t<post id=\"".($postID)."\">\n";
	print OUT "\t<title> $title </title>\n";
	print OUT "\t<body> ",$body," </body>\n";
	print OUT "\t<comments>\n"; 
	$index=0;
	foreach (@comments){
		print OUT "\t\t<comment id=\"$index\" responses=\"",$responses[$index],"\">$_</comment>\n";
		$index++;
	}

	print OUT "\t<\/comments>\n";
	print OUT "\t<\/post>\n";
	print OUT "<\/posts>\n";



